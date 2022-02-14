import java.io.IOException;

import org.json.JSONException;

import Content.Configuration;

public class main {
	public static void main(String args[]) throws JSONException, IOException {
		System.out.println(Configuration.getInstance().loadDefaultConfig());
	}
}
