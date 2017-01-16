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
 * Created by calata on 12/01/17.
 *
 * GenericArgumentIdDataFetcher
 * DataFetcher to recover an embedded resource related to another parent resource
 * from the SW API REST
 * Embedded resource could be an individual resource or a list, in which case must return a Connection
 *
 */
@Component
@Scope("prototype")
public class GenericEmbeddedDataFetcher implements DataFetcher {

    @Autowired
    private RestService restService;

    private String relationshipName;
    private Boolean connection = Boolean.FALSE;

    @Override
    public Object get(DataFetchingEnvironment environment) {

        if (environment.getSource() != null) {
            Map source = (Map) environment.getSource();
            if (connection) {
                return resolveConnection(source, environment);
            } else {
                return resolveEntity(source);
            }
        }
        return null;
    }

    /**
     * If relationship relates to an individual resource, call for REST service
     * @param source
     * @return
     */
    private Object resolveEntity(Map source) {
        return restService.restRequest(getURI(source), false);
    }

    /**
     * If relationship relates to a list resource, call REST service for every resource
     * and return a Connection object
     * @param source
     * @param environment
     * @return
     */
    private Object resolveConnection(Map source, DataFetchingEnvironment environment) {
        List<String> uris;
        List<Object> objectList;

        uris = getURIs(source);

        if (uris == null) {
            return null;
        } else if (uris.size() > 0) {
            objectList = new ArrayList<>();
            for (String uri : uris) {
                objectList.add(restService.restRequest(uri, false));
            }
            return new SimpleListConnection(objectList).get(environment);
        }

        return null;
    }

    private List<String> getURIs(Map source){
        return (List<String>) source.get(getRelationshipName());
    }

    private String getURI(Map source){
        return (String) source.get(getRelationshipName());
    }

    private String getRelationshipName(){
        return relationshipName;
    }

    /** Every service instance must define relationship name
        This is the name of the object in parent
        Example: A character defines the films relationship as

        "skin_color": "blonde",
        "films": [
				"http://swapi.co/api/films/6/",
				"http://swapi.co/api/films/3/",
				...
			],
		...

        Service instantiation should call x.setRelationshipName("films")

     */
    public void setRelationshipName(String relationshipName){
        this.relationshipName = relationshipName;
    }


    /**
     * Every service instance must define if relationship is a list or not
     */
    public void setConnection(Boolean connection){
        this.connection = connection;
    }

}
