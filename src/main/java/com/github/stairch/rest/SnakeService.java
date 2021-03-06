package com.github.stairch.rest;

import com.github.stairch.dtos.*;
import com.github.stairch.types.HeadType;
import com.github.stairch.types.Move;
import com.github.stairch.types.TailType;
import com.google.gson.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static com.github.stairch.RestInPeace.BASE_URI;

@Path("/")
public class SnakeService {

    /**
     * Used for json serialization/deserialization.
     */
    private final Gson gson = new Gson();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "yeaay, your starter snake is up and running :)";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/start")
    public final Response start(final StartRequestDTO startRequestDTO) {
        System.out.println(startRequestDTO);

        final StartResponseDTO startResponse = new StartResponseDTO();
        startResponse.setColor("yellow");
        startResponse.setHeadUrl("https://i.pinimg.com/originals/cd/29/a7/cd29a717a709e8a61ed69bf3f8d69df8.jpg");
        startResponse.setName("Banana Snake");
        startResponse.setTaunt("Ooooh Bananas!");

        startResponse.setHeadType(HeadType.getFang());
        startResponse.setTailType(TailType.getBlockbum());
        final String responseBody = gson.toJson(startResponse);
        return Response.status(Response.Status.OK).entity(responseBody).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/move")
    public final Response move(String string) {
        MoveRequestDTO moveRequestDTO = this.getMoveRequestDTO(string);

        PathFinderDaniel pathFinder = new PathFinderDaniel(moveRequestDTO.getWidth(), moveRequestDTO.getHeight(), moveRequestDTO.getYou());

        final MoveResponseDTO moveResponse = new MoveResponseDTO();
        SnakeDTO ownSnake = pathFinder.getOwnSnake(moveRequestDTO.getYou(), moveRequestDTO.getSnakes());
        PointDTO closestFood = pathFinder.getClosestFood(ownSnake, moveRequestDTO.getFood());
        Move move = pathFinder.moveToFood(ownSnake, closestFood, moveRequestDTO.getSnakes());

        if(move != null){
            moveResponse.setMove(move);
        }
        else{
            moveResponse.setMove(Move.right);
        }

        moveResponse.setTaunt("Ooooh Bananas!");

        final String responseBody = gson.toJson(moveResponse);
        return Response.status(Response.Status.OK).entity(responseBody).build();
    }

    private MoveRequestDTO getMoveRequestDTO(String string) {
        MoveRequestDTO moveRequestDTO = gson.fromJson(string, MoveRequestDTO.class);
        JsonParser jsonParser = new JsonParser();
        JsonObject json = (JsonObject) jsonParser.parse(string);

        JsonArray snakes = json.get("snakes").getAsJsonArray();
        for (SnakeDTO snakeDTO : moveRequestDTO.getSnakes()) {
            this.saveCoordsForSnake(snakes, snakeDTO);
        }

        JsonArray food = json.get("food").getAsJsonArray();
        ArrayList<PointDTO> pointDTOS = new ArrayList<>();
        for (JsonElement jsonElement : food) {
            JsonArray foodCoords = jsonElement.getAsJsonArray();
            PointDTO pointDTO = new PointDTO();
            pointDTO.setX(foodCoords.get(0).getAsInt());
            pointDTO.setY(foodCoords.get(1).getAsInt());
            pointDTOS.add(pointDTO);
        }
        moveRequestDTO.setFood(pointDTOS);

        JsonArray deadSnakes = json.get("dead_snakes").getAsJsonArray();
        for (SnakeDTO snakeDTO : moveRequestDTO.getDeadSnakes()) {
            this.saveCoordsForSnake(deadSnakes, snakeDTO);
        }

        return moveRequestDTO;
    }

    private void saveCoordsForSnake(JsonArray snakes, SnakeDTO snakeDTO) {
        for (JsonElement snake : snakes) {
            JsonElement id = snake.getAsJsonObject().get("id");
            if (snakeDTO.getId().equals(id.getAsString())) {
                JsonObject asJsonObject = snake.getAsJsonObject();
                JsonArray coords = asJsonObject.get("coords").getAsJsonArray();
                List<PointDTO> pointDTOS = new ArrayList<>();
                for (JsonElement coord : coords) {
                    JsonArray coordArray = coord.getAsJsonArray();
                    PointDTO pointDTO = new PointDTO();
                    pointDTO.setX(coordArray.get(0).getAsInt());
                    pointDTO.setY(coordArray.get(1).getAsInt());
                    pointDTOS.add(pointDTO);
                }
                snakeDTO.setCoordinates(pointDTOS);
            }
        }
    }
}