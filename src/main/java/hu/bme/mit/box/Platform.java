package hu.bme.mit.box;

import hu.bme.mit.box.data.Database;
import hu.bme.mit.box.data.LocalData;
import hu.bme.mit.box.data.impl.Neo4JDatabase;
import hu.bme.mit.box.plugin.PluginManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.List;

public class Platform extends AbstractVerticle {

    public static final PluginManager pluginManager = new PluginManager();
    public static Vertx vertx;
    public static Database database;
    public static LocalData localData;
    public static Logger logger = LoggerFactory.getLogger("hu.bme.mit.box");

    public Platform(String url, String username, String password) {
        int cores = CpuCoreSensor.availableProcessors();
        VertxOptions op = new VertxOptions();
        op.setWorkerPoolSize(2*cores); //rethinking architecture; might be unnecessary
        vertx = Vertx.vertx(op);
        vertx.deployVerticle(this);
        database = new Neo4JDatabase();
        vertx.deployVerticle(database);
        pluginManager.loadExtantPlugins();
        localData = new LocalData();
    }

    public static void main(String[] args) {
        CLI cli = CLI.create("box")
                .addOption(new Option().setLongName("help").setShortName("h").setDescription("This thing.").setFlag(true))
                .addOption(new Option().setLongName("username").setShortName("u").setDescription("The username to be used with the database."))
                .addOption(new Option().setLongName("password").setShortName("p").setDescription("The password to be used with the database."))
                .addOption(new Option().setLongName("url").setShortName("l").setDescription("The URL (location) of the databse.")); //TODO defaults
        CommandLine commandLine = cli.parse(List.of(args));
        if(!commandLine.isFlagEnabled("help")){
            new Platform(commandLine.getOptionValue("url"), commandLine.getOptionValue("username"), commandLine.getOptionValue("password"));
        } else {
            StringBuilder builder = new StringBuilder();
            cli.usage(builder);
            logger.info(builder.toString());
        }
    }
}
