package com.byteflair.graphql.schema.type;

import graphql.relay.Relay;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLTypeReference;
import graphql.schema.TypeResolver;

import java.util.ArrayList;

/**
 * Created by calata on 13/01/17.
 *
 * FilmConnection Type
 * Defines a list of Film type with relay structure
 *
 * edges{
      cursor
      node{
         _field_
      }
    }
 *
 */
public class FilmConnection {

    public GraphQLObjectType buildFilmConnection(){
        Relay relay = new Relay();
        GraphQLInterfaceType NodeInterface = relay.nodeInterface(new TypeResolver() {
            @Override
            public GraphQLObjectType getType(Object object) {
                Relay.ResolvedGlobalId resolvedGlobalId = relay.fromGlobalId((String) object);

                return null;
            }
        });

        // Second argument looks into schema for Film type, which must be defined
        GraphQLObjectType filmEdgeType = relay.edgeType("Film",  new GraphQLTypeReference("Film"), NodeInterface, new ArrayList<>());

        // First argument concatenates with "Connection" string to define "FilmConnection" type in schema dictionary
        return relay.connectionType("Film", filmEdgeType, new ArrayList<>());
    }

}
