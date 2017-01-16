package com.byteflair.controller;

/**
 * Created by calata on 21/12/16.
 */

import com.byteflair.graphql.schema.ApiSchema;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class GraphQLController {

    private static final Logger log = LoggerFactory.getLogger(GraphQLController.class);

    @Autowired
    private ApiSchema apiSchema;
    private GraphQL graphql;

    /**
     * Unique access point to the GraphQL API
     * All requests will begin here.
     * @param body
     * @return
     */
    @RequestMapping(value = "/graphql", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object executeOperation(@RequestBody Map body) {
        graphql = new GraphQL(apiSchema.buildSchema());
        String query = (String) body.get("query");
        Map<String, Object> variables = (Map<String, Object>) body.get("variables");
        ExecutionResult executionResult = null;
        if (variables == null){
            executionResult = graphql.execute(query);
        } else {
            executionResult = graphql.execute(query, (Object)null, variables);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        if (executionResult.getErrors().size() > 0) {
            result.put("errors", executionResult.getErrors());
            log.error("Errors: {}", executionResult.getErrors());
        }
        result.put("data", executionResult.getData());
        return result;
    }



}
