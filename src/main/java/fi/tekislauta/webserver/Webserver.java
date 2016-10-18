package fi.tekislauta.webserver;

import com.google.gson.Gson;
import fi.tekislauta.db.Database;
import fi.tekislauta.db.objects.BoardDao;
import fi.tekislauta.db.objects.PostDao;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.DatabaseObject;

import static spark.Spark.*;

import static spark.Spark.get;

import java.util.Map;

public class Webserver {

    private final int port;
    private final Gson gson;
    private final Database db;
    private final BoardDao boardDao;
    private final PostDao postDao;

    public Webserver(int port) {
        this.port = port;
        this.gson = new Gson();
        this.db = new Database();
        this.boardDao = new BoardDao();
        this.postDao = new PostDao();
    }

    public void listen() {
        port(this.port);

        // spark starts listening when first method listener is added, I think ~cx
        get("/api/boards/", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            return gson.toJson(boardDao.fetchAll(db, ""));
        });

        get("/api/boards/:abbreviation", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            return gson.toJson(boardDao.fetch(db, req.params("abbreviation")));
        });

        get("/api/boards/posts/:abbreviation", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            return "";
        });

        get("/api/boards/:board/posts/", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            return gson.toJson(postDao.fetchAll(db, req.params("board")));
        });

        get("/api/jerry", (req, res) -> {
            return "\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40 good shit go౦ԁ sHit\uD83D\uDC4C thats ✔ some good\uD83D\uDC4C\uD83D\uDC4Cshit right\uD83D\uDC4C\uD83D\uDC4Cthere\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4C right✔there ✔✔if i do ƽaү so my self \uD83D\uDCAF i say so \uD83D\uDCAF thats what im talking about right there right there (chorus: ʳᶦᵍʰᵗ ᵗʰᵉʳᵉ) mMMMMᎷМ\uD83D\uDCAF \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4CНO0ОଠOOOOOОଠଠOoooᵒᵒᵒᵒᵒᵒᵒᵒᵒ\uD83D\uDC4C \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4C \uD83D\uDCAF \uD83D\uDC4C \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC4C\uD83D\uDC4CGood shit";
        });

        post("/api/boards/", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            Map json = gson.fromJson(req.body(), Map.class);
            Board b = new Board();
            b.setName((String)json.get("name"));
            b.setAbbreviation((String)json.get("abbreviation"));
            b.setDescription((String)json.get("description"));
            return gson.toJson(boardDao.post(db, b));
        });
    }
}