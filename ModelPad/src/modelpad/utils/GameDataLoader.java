package modelpad.utils;

import java.io.IOException;
import java.io.InputStream;

import modelpad.metamodel.Level;
import android.content.Context;
import android.content.res.AssetManager;

import com.thoughtworks.xstream.XStream;

public class GameDataLoader {

	public static Level getLevelByFilePath(Context ctx, String path) throws IOException {
		AssetManager asset = ctx.getAssets();
		InputStream stream = asset.open(path);
		XStream xstream = XStreamHelper.getConfiguredXStream();
		Level level = (Level) xstream.fromXML(stream);
		return level;
	}
}
