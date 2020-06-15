package net.guavy.sf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.guavy.sf.command.NewPostCommand;
import net.guavy.sf.data.PacketHandler;
import net.guavy.sf.data.Post;
import net.guavy.sf.event.PauseEventHandler;
import net.guavy.sf.event.ScreenActionEventHandler;
import net.guavy.sf.event.ScreenshotEventHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

@Mod(modid = ScreenshotForum.MODID, name = ScreenshotForum.NAME, version = ScreenshotForum.VERSION)
public class ScreenshotForum {
    public static final String MODID = "sf";
    public static final String NAME = "Screenshot Forum";
    public static final String VERSION = "1.0";

    public static final String IMGUR_TOKEN = "";

    private static Logger logger;

    private static File worldDirectory;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new PauseEventHandler());
        MinecraftForge.EVENT_BUS.register(new ScreenshotEventHandler());
        MinecraftForge.EVENT_BUS.register(new ScreenActionEventHandler());

        PacketHandler.registerMessages("sf");

        ClientCommandHandler.instance.registerCommand(new NewPostCommand());
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        worldDirectory = new File("saves", event.getServer().getWorldName());

        if(getPosts().isEmpty()) {
            ArrayList<Post> posts = new ArrayList<>();
            posts.add(new Post("Sample Post", "A sample post! Create your own by taking a screenshot in game.", "Geometrically", null, 1592194220));

            writePosts(posts);
        }
    }



    public static ArrayList<Post> getPosts() {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(getPostsPath()));

            return gson.fromJson(reader, new TypeToken<ArrayList<Post>>(){}.getType());
        } catch (FileNotFoundException exception) {
            logger.error(exception.getMessage());

            return new ArrayList<>();
        }
    }

    public static byte[] getPostsAsBytes() {
        try {
            return Files.readAllBytes(getPostsPath().toPath());
        } catch (IOException exception) {
            logger.error(exception.getMessage());
            return new byte[]{};
        }
    }

    public static ArrayList<Post> getPostsFromBytes(byte[] bytes) {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(bytes)));

        return gson.fromJson(reader, new TypeToken<ArrayList<Post>>(){}.getType());
    }

    public static void writePosts(ArrayList<Post> posts) {
        try {
            File file = getPostsPath();
            file.createNewFile();

            String string = new Gson().toJson(posts);

            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);

            myOutWriter.append(string);

            myOutWriter.close();
            fOut.close();
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }
    }

    public static File getPostsPath()
    {
        return new File(worldDirectory,"posts.json");
    }
}
