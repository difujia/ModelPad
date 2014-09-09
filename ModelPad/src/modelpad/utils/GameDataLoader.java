package modelpad.utils;

import java.io.IOException;
import java.io.InputStream;

import modelpad.datamodel.Level;
import android.content.Context;
import android.content.res.AssetManager;

import com.thoughtworks.xstream.XStream;

public class GameDataLoader {

	private GameDataLoader() {}

	public static Level getLevelByFilePath(Context ctx, String path) throws IOException {
		AssetManager asset = ctx.getAssets();
		InputStream stream = asset.open(path);
		XStream xstream = XStreamHelper.getReusableConfiguredXStream();
		Level level = (Level) xstream.fromXML(stream);
		return level;
	}
}
