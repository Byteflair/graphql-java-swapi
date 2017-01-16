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
public class FilmType {

    @Autowired
    private GenericEmbeddedDataFetcher planetsEmbeddedDataFetcher;
    @Autowired
    private GenericEmbeddedDataFetcher charactersEmbeddedDataFetcher;

    public GraphQLObjectType buildFilmType(){

        // Each GenericEmbeddedDataFetcher service must define its relationshipName and connection variables
        planetsEmbeddedDataFetcher.setRelationshipName("planets");
        planetsEmbeddedDataFetcher.setConnection(Boolean.TRUE);
        charactersEmbeddedDataFetcher.setRelationshipName("characters");
        charactersEmbeddedDataFetcher.setConnection(Boolean.TRUE);

        Relay relayConnection = new Relay();


        return newObject()
            .name("Film")
            .description("Film - Represents an SW film")
            .field(newFieldDefinition()
                       .name("director")
                       .description("Director of the film")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("created")
                       .description("Date of DB insertion")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("episode_id")
                       .description("API Id of the film")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("opening_crawl")
                       .description("Intro of the film")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("producer")
                       .description("Productor of the film")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("release_date")
                       .description("Release date of the film")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("title")
                       .description("Title")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("url")
                       .description("URI of the element")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("planetsConnection")
                       .description("Planets appearing in the film")
                       .type(new GraphQLTypeReference("PlanetConnection")) // Will look into schema for PlanetConnection type
                       .argument(relayConnection.getConnectionFieldArguments()) // add first,last,before,next arguments to connection
                       .dataFetcher(planetsEmbeddedDataFetcher) // Planets relationship DataFetcher
                       .build())
            .field(newFieldDefinition()
                       .name("charactersConnection")
                       .description("Characters appearing in the film")
                       .type(new GraphQLTypeReference("PeopleConnection"))
                       .argument(relayConnection.getConnectionFieldArguments())
                       .dataFetcher(charactersEmbeddedDataFetcher)
                       .build())
            .build();
    }
}
