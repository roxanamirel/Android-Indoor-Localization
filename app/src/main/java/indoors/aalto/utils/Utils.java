package indoors.aalto.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import indoors.aalto.json.models.Measurements;

/**
 * Created by rroxa_000 on 11/30/2015.
 */
public class Utils {

    private static final String TAG = "Util";

    public static String loadJSONfromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = 0;
            size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<String> createReferencePathDir() {
        List<String> dirs = new ArrayList<>();
        dirs.add(DirPath.A112.name());
        dirs.add(DirPath.A118.name());
        dirs.add(DirPath.A124.name());
        dirs.add(DirPath.A128.name());
        dirs.add(DirPath.A136.name());
        dirs.add(DirPath.A141.name());
        return dirs;

    }

    public static List<Measurements> importData(String dir_path, Context context) {
        ObjectMapper mapper = new ObjectMapper();
        List<Measurements> measurements = new ArrayList<>();
        String fileType = "json";
        try {
            String[] files = context.getAssets().list(dir_path);
            for (int i = 0; i < files.length; i++) {
                if (files[i].contains(fileType)) {
                    String measurement_data1Json = Utils.loadJSONfromAsset(context,
                            dir_path + "/" + files[i]);
                    measurements.add(mapper.readValue(measurement_data1Json, Measurements.class));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return measurements;
    }

    public static Map<String, List<Measurements>> importReferenceMeasures(Context context) {
        Map<String, List<Measurements>> refMeasurements = new HashMap<>();
        for (String dir : Utils.createReferencePathDir()) {
            List<Measurements> reference_data = importData(DirPath.reference_data.name() + "/" + dir, context);
            refMeasurements.put(dir, reference_data);
        }
        return refMeasurements;
    }

    public static void writeMeasurementsToJSONToFile(Context context, Measurements measurements) {

        if (isExternalStorageWritable()) {
            try {
                String json = new ObjectMapper().writeValueAsString(measurements );
                File file = getStorageDir(context, "own-measurements");
                File myFile = new File(file, "own-measuremets.txt");
                FileOutputStream f = new FileOutputStream(myFile);
                PrintWriter pw = new PrintWriter(f);
                pw.println(json);
                pw.flush();
                pw.close();
                f.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Exception", "JSON File write failed: " + e.toString());
            }

        } else {
            Log.e(TAG, "File not writable");
        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static File getStorageDir(Context context, String name) {

        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), name);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }
}
