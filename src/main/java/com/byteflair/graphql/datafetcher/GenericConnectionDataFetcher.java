package com.byteflair.graphql.datafetcher;

import com.byteflair.rest.RestService;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by calata on 13/01/17.
 *
 * GenericConnectionDataFetcher
 * DataFetcher to recover a list of resource from the SW API REST
 * and return it as a GraphQL Connection object
 */
@Component
@Scope("prototype")
public class GenericConnectionDataFetcher implements DataFetcher{

    @Autowired
    private RestService restService;

    private String URI;

    @Override
    public Object get(DataFetchingEnvironment environment) {
        List<Object> list = new ArrayList<>();
        Map page = (Map) restService.restRequest(getURI(), true);

        /**
         * SW API response list format
         * {
            "count": 1,
            "next": "http://swapi.co/api/_resource_/?page=2",
            "previous": null,
            "results": [
                ...
            ]
           }
        */
        while (page != null){

            List results = (List) page.get("results");
            String next = (String) page.get("next");

            for (Object r : results){
                list.add(r);
            }
            page = null;

            if (next != null) {
                page = (Map) restService.restRequest(next, false);
            }
        }

        if (list != null) {
            // Return a Connection object
            return new SimpleListConnection(list).get(environment);
        }

        return null;
    }

    public String getURI(){
        return URI;
    }

    /** Every service instance must define relative URI
        Example: "/films/"
     */
    public void setURI(String URI){
        this.URI = URI;
    }

}
