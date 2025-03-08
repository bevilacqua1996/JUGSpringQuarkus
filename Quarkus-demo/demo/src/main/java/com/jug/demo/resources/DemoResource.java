package com.jug.demo.resources;

import com.jug.demo.entities.DemoEntity;
import com.jug.demo.repositories.DemoEntityRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/demo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DemoResource {

    private final DemoEntityRepository repository;

    public DemoResource(DemoEntityRepository repository) {
        this.repository = repository;
    }

    @GET
    public List<DemoEntity> getAll() {
        return repository.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        DemoEntity existingEntity = repository.findById(id);
        if (existingEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(existingEntity).build();
    }

    @POST
    @Transactional
    public Response create(DemoEntity demoEntity) {
        repository.persist(demoEntity);
        return Response.status(Response.Status.CREATED).entity(demoEntity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, DemoEntity updatedEntity) {
        DemoEntity existingEntity = repository.findById(id);
        if (existingEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existingEntity.setName(updatedEntity.getName());
        return Response.ok(existingEntity).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        DemoEntity entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        repository.delete(entity);
        return Response.noContent().build();
    }
}