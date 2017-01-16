package com.byteflair.graphql.schema.type;

import com.byteflair.graphql.datafetcher.GenericEmbeddedDataFetcher;
import graphql.Scalars;
import graphql.relay.Relay;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

/**
 * Created by calata on 12/01/17.
 */

@Component
public class PlanetType {

    @Autowired
    private GenericEmbeddedDataFetcher filmsEmbeddedDataFetcher;
    @Autowired
    private GenericEmbeddedDataFetcher residentsEmbeddedDataFetcher;


    public GraphQLObjectType buildPlanetType() {

        // Each GenericEmbeddedDataFetcher service must define its relationshipName and connection variables
        filmsEmbeddedDataFetcher.setRelationshipName("films");
        filmsEmbeddedDataFetcher.setConnection(Boolean.TRUE);
        residentsEmbeddedDataFetcher.setRelationshipName("residents");
        residentsEmbeddedDataFetcher.setConnection(Boolean.TRUE);

        Relay relayConnection = new Relay();

        return newObject()
            .name("Planet")
            .description("Planet - Represents an SW planet")
            .field(newFieldDefinition()
                       .name("climate")
                       .description("Climate of the planet")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("diameter")
                       .description("Diameter of the planet (km)")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("gravity")
                       .description("Gravity force of the planet")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("name")
                       .description("Name of the planet")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("population")
                       .description("Number of inhabitants of the planet")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("orbital_period")
                       .description("Number of days the planet takes to complete an orbit around his star")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("rotation_period")
                       .description("Number of hours the planet takes to complete a single rotation on its axis")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("surface_water")
                       .description("Percent of planet covered by water")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("terrain")
                       .description("Terrain of the planet")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("created")
                       .description("API insertion date")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("edited")
                       .description("API last modification date")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("url")
                       .description("URI of the element")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("residentsConnection")
                       .description("Characters that lives in the planet")
                       .type(new GraphQLTypeReference("PeopleConnection")) // Will look into schema for PeopleConnection type
                       .argument(relayConnection.getConnectionFieldArguments()) // add first,last,before,next arguments to connection
                       .dataFetcher(residentsEmbeddedDataFetcher) // Residents relationship DataFetcher
                       .build())
            .field(newFieldDefinition()
                       .name("filmsConnection")
                       .description("Films which planet appears")
                       .type(new GraphQLTypeReference("FilmConnection"))
                       .argument(relayConnection.getConnectionFieldArguments())
                       .dataFetcher(filmsEmbeddedDataFetcher)
                       .build())
            .build();
    }
}
