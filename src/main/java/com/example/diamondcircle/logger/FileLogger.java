package com.example.diamondcircle.logger;

import java.io.IOException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;



public class FileLogger {
    public static final Logger logger = Logger.getLogger("DiamondCircle-Logger");
    private static Handler handler;

    static {
        try {
            handler = new FileHandler("src"+File.separator+"main"+File.separator+"java"+File.separator+
                    "com"+File.separator+"example"+File.separator+"diamondcircle"+File.separator+"log"+File.separator+"mylogs.log", true);
            logger.addHandler(handler);
            logger.getLogger(FileLogger.class.getName()).setUseParentHandlers(false);
        }
        catch(IOException exception)
        {
            logger.severe(exception.fillInStackTrace().toString());
        }
        catch (SecurityException exception)
        {
            logger.severe(exception.fillInStackTrace().toString());
        }
    }

    public static void log(Throwable ex)
    {
        logger.severe(ex.fillInStackTrace().toString());
    }

}
