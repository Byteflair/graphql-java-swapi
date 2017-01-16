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
 * PeopleConnection Type
 * Defines a list of People type with relay structure
 *
 * edges{
        cursor
        node{
            _field_
        }
    }
 *
 */
public class PeopleConnection {

    public GraphQLObjectType buildPeopleConnection(){
        Relay relay = new Relay();
        GraphQLInterfaceType NodeInterface = relay.nodeInterface(new TypeResolver() {
            @Override
            public GraphQLObjectType getType(Object object) {
                Relay.ResolvedGlobalId resolvedGlobalId = relay.fromGlobalId((String) object);

                return null;
            }
        });


        // Second argument looks into schema for People type, which must be defined
        GraphQLObjectType peopleEdgeType = relay.edgeType("People",  new GraphQLTypeReference("People"), NodeInterface, new ArrayList<>());

        // First argument concatenates with "Connection" string to define "PeopleConnection" type in schema dictionary
        return relay.connectionType("People", peopleEdgeType, new ArrayList<>());
    }
}
