package resources;

import data.messages.ReturnMessage;
import data.messages.ReturnMessageBook;
import database.DatabaseHandler;
import database.DatabaseHandlerBook;
import models.BookRequirements;
import models.Book;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Set;

@Path("/books")
public class ResourceBook {

    @ConfigProperty(name = "bookTable")
    String tableName;

    @Inject
    Logger logger;

    @Inject
    Validator validator;

    @Inject
    DatabaseHandler databaseHandler;

    @Inject
    DatabaseHandlerBook databaseHandlerBook;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecords() {
        logger.info("GET -> Trying to get all the books.");
        try {
            ReturnMessageBook response = databaseHandlerBook.getBooks(tableName);
            if (response.isValid()) {
                logger.info("Got all the books successfully.");
                return Response.ok(response.getResult()).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("Database Error: " + e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createResource(Book newBook) {
        logger.info("POST -> Trying to create a new book.");
        Set<ConstraintViolation<Book>> violations = validator.validate(newBook);
        if (violations.isEmpty()) {
            ReturnMessage response = databaseHandlerBook.addBook(tableName, newBook);
            if (response.isValid()) {
                logger.info("Book added correctly.");
                return Response.ok(response.getMessage()).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
            }
        } else {
            StringBuilder violationsString = new StringBuilder();
            for (ConstraintViolation<Book> book : violations) {
                violationsString.append(" ").append(book.getMessage()).append("\n");
            }
            logger.error(violationsString.toString());
            return Response.status(Response.Status.BAD_REQUEST).entity(violationsString.toString()).build();
        }
    }

    @PUT
    @Path("{id}/{parameterToChange}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updateResource(
            @PathParam("id") Integer id,
            @PathParam("parameterToChange") String param,
            @QueryParam("value") String valueToSet) {
        logger.info("PUT -> Trying to update the book with the id " + id + ".");
        Set<ConstraintViolation<Book>> violations;
        try {
            Book newBook = new Book(null, null, null, null, null, null, null);
            Method method = Book.class.getDeclaredMethod("set" + param.substring(0, 1).toUpperCase() + param.substring(1), String.class);
            method.invoke(newBook, valueToSet);
            violations = validator.validate(newBook);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            logger.error(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("Format error: Wrong parameter name.").build();
        }

        if (violations.isEmpty()) {
            ReturnMessage response = databaseHandler.updateResource(tableName, id, param, valueToSet);
            if (response.isValid()) {
                logger.info("Book with the id " + id + " updated successfully.");
                return Response.ok(response.getMessage()).build();
            } else
                return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
        } else {
            StringBuilder violationsString = new StringBuilder();
            for (ConstraintViolation<Book> book : violations) {
                violationsString.append(" ").append(book.getMessage()).append("\n");
            }
            logger.error(violationsString.toString());
            return Response.status(Response.Status.BAD_REQUEST).entity(violationsString.toString()).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deleteResource(@PathParam("id") Integer id) {
        logger.info("DELETE -> Trying to delete the book with the id " + id + ".");
        ReturnMessage response = databaseHandler.deleteResource(tableName, id);
        if (response.isValid()) {
            logger.info(response.getMessage());
            return Response.ok(response.getMessage()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
        }
    }

    @POST
    @Path("/filter/{logic}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response filterResources(@PathParam("logic") String logic, BookRequirements requirements) {
        logger.info("POST -> Trying to filter the books set.");
        if (!logic.equals("AND") && !logic.equals("OR")) {
            logger.error("Wrong logic name.");
            return Response.status(Response.Status.BAD_REQUEST).entity("URL error: Wrong logic name.").build();
        }
        ReturnMessageBook response = databaseHandlerBook.filterBook(tableName, requirements, logic);
        if (response.isValid()) {
            logger.info("Books set filtered correctly.");
            if (response.getResult().isEmpty()) {
                return Response.ok("There are not any records fulfilling your requirements. " +
                        "Check your input for typos or change your requirements.").build();
            } else {
                return Response.ok(response.getResult()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
        }
    }
}
