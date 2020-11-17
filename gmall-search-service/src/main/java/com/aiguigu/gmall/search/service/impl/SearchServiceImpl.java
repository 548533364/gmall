package com.aiguigu.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import com.atguigu.gmall.bean.PmsSearchParam;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    @Override
    public List < PmsSearchSkuInfo > list ( PmsSearchParam pmsSearchParam ) {
        String dslStr = getSearchDsl ( pmsSearchParam );
        System.err.println ( dslStr );
        // 用api执行复杂查询
        List < PmsSearchSkuInfo > pmsSearchSkuInfos = new ArrayList <> ( );
        Search search = new Search.Builder ( dslStr ).addIndex ( "gmallpms" ).addType ( "PmsSkuInfo" ).build ( );
        SearchResult execute = null;
        try {
            execute = jestClient.execute ( search );
        } catch (IOException e) {
            e.printStackTrace ( );
        }

        List < SearchResult.Hit < PmsSearchSkuInfo, Void > > hits = execute.getHits ( PmsSearchSkuInfo.class );
        for (SearchResult.Hit < PmsSearchSkuInfo, Void > hit : hits) {
            PmsSearchSkuInfo source = hit.source;
            Map < String, List < String > > highlight = hit.highlight;
            /**高亮是在搜索关键字前提下触发，没有搜索关键字就不触发【解析高亮的前提是highlight不为空】**/
            if (highlight!=null){
                String skuName = highlight.get ( "skuName" ).get ( 0 );
                source.setSkuName ( skuName );
            }
            /**未修改前，报错空指针异常**/
            // Map < String, List < String > > highlight = hit.highlight;
            // String skuName = highlight.get ( "skuName" ).get ( 0 );
            // source.setSkuName ( skuName );
            pmsSearchSkuInfos.add ( source );
        }

        System.out.println ( pmsSearchSkuInfos.size ( ) );
        return pmsSearchSkuInfos;
    }
    private String getSearchDsl ( PmsSearchParam pmsSearchParam ) {

        List < PmsSkuAttrValue > skuAttrValueList = pmsSearchParam.getSkuAttrValueList ( );
        String keyword = pmsSearchParam.getKeyword ( );
        String catalog3Id = pmsSearchParam.getCatalog3Id ( );

        // jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder ( );
        // bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder ( );

        // filter
        if (StringUtils.isNotBlank ( catalog3Id )) {
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder ( "catalog3Id",catalog3Id );
            boolQueryBuilder.filter ( termQueryBuilder );
        }
        if (skuAttrValueList != null) {
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder ( "skuAttrValueList.valueId",pmsSkuAttrValue.getValueId ( ) );
                boolQueryBuilder.filter ( termQueryBuilder );
            }
        }


        // query
        searchSourceBuilder.query ( boolQueryBuilder );

        // highlight
       HighlightBuilder highlightBuilder = new HighlightBuilder ( );
       /**以下三行代码是高亮**/
        highlightBuilder.preTags ( "<span style='color:red;'>" );
        highlightBuilder.field ( "skuName" );
        highlightBuilder.postTags ( "</span>" );
        searchSourceBuilder.highlighter ( highlightBuilder );
        // sort
        searchSourceBuilder.sort ( "id",SortOrder.DESC );
        // from
        searchSourceBuilder.from ( 0 );
        // size
        searchSourceBuilder.size ( 10 );

        // must
        if (StringUtils.isNotBlank ( keyword )) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder ( "skuName",keyword );
            boolQueryBuilder.must ( matchQueryBuilder );
        }
        /** java聚合代码**/
        //创建聚合函数，函数名叫“groupBy_attr”，字段是“skuAttrValueList.valueId”
        TermsAggregationBuilder groupby_attr = AggregationBuilders.terms ( "groupBy_attr" ).field ( "skuAttrValueList.valueId" );
        searchSourceBuilder.aggregation ( groupby_attr );

        return searchSourceBuilder.toString ( );

    }
    /**
     * 搜索页面的平台属性列表是从搜索结果中抽取出来的，不是根据三级分类ID查询的所有平台属性的集合
     *    解决方法
     *          1、es中使用aggregations聚合函数抽取平台属性
     *
     *          2、使用java代码抽取平台属性《这个性能会更高》
     */


}
