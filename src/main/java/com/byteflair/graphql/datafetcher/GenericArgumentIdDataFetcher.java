package com.byteflair.graphql.datafetcher;

import com.byteflair.rest.RestService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by calata on 12/01/17.
 *
 * GenericArgumentIdDataFetcher
 * DataFetcher to recover an individual resource from the SW API REST
 * looking for an URI /api/_resource_/_id_
 */
@Component
@Scope("prototype")
public class GenericArgumentIdDataFetcher implements DataFetcher {

    @Autowired
    private RestService restService;

    private String URI;

    @Override
    public Object get(DataFetchingEnvironment environment) {
        // Extract 'id' argument from the GraphQL execution environment
        if (environment.getArgument("id") != null){
            // Call REST Service
            return restService.restRequest(getURI()
                                           .concat(environment.getArgument("id").toString())
                                           .concat("/"), true);
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
