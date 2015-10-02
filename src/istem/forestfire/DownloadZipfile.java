package istem.forestfire;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Hp on 6/19/2015.
 */
class DownloadZipfile extends AsyncTask<String, String, String>
{
    private final Context mContext;
    private AsyncResponse delegate=null;
    String result ="";
    private ProgressDialog mProgressDialog;
    public static String file_url="http://atnepal.com/forest_fire/";
    String unzipLocation = Environment.getExternalStorageDirectory().getPath()+"/";
    String StorezipFileLocation =Environment.getExternalStorageDirectory().getPath() + "/DownloadedZip";
    String DirectoryName=Environment.getExternalStorageDirectory().getPath()+"/";

    public DownloadZipfile(Context context)
    {
        mContext=context;
    }
    public DownloadZipfile(Context context,AsyncResponse response)
    {
        this.mContext=context;
        this.mProgressDialog=new ProgressDialog(mContext);
        this.delegate=response;
    }

    protected void onPreExecute()
    {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Downloading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... aurl)
    {
        int count;
        try
        {
            URL url = new URL(aurl[0]);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(StorezipFileLocation);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1)
            {
                total += count;
                publishProgress(""+(int)((total*100)/lenghtOfFile));
                output.write(data, 0, count);
            }
            output.close();
            input.close();
            result = "true";
        }
        catch (Exception e)
        {
            result = "false";
        }
        return null;
    }

    protected void onProgressUpdate(String... progress)
    {
        Log.d("ANDRO_ASYNC", progress[0]);
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String unused)
    {
        mProgressDialog.dismiss();
        if(result.equalsIgnoreCase("true"))
        {
            try
            {
                unzip();
            }
            catch (IOException e)
            {
                Log.e("unzip_error", e.toString());
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
        }
    }

    public void unzip() throws IOException
    {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        new UnZipTask().execute(StorezipFileLocation, DirectoryName);
    }

private class UnZipTask extends AsyncTask<String, Void, Boolean> {
    @SuppressWarnings("rawtypes")
    @Override
    protected Boolean doInBackground(String... params) {
        Log.i("unzip", "start");
        String filePath = params[0];
        String destinationPath = params[1];
        File archive = new File(filePath);
        try {
            ZipFile zipfile = new ZipFile(archive);
            for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                unzipEntry(zipfile, entry, destinationPath);
            }
            UnzipUtil d = new UnzipUtil(StorezipFileLocation, DirectoryName);
            d.unzip();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected void onPostExecute(Boolean result) {
        delegate.processFinish(result);
        mProgressDialog.dismiss();
    }

    private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException {
        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }
        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            createDir(outputFile.getParentFile());
        }
        // Log.v("", "Extracting: " + entry);
        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
        try {
        } finally {
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }
    }

    private void createDir(File dir) {
        if (dir.exists()) {
            return;
        }
        if (!dir.mkdirs()) {
            throw new RuntimeException("Can not create dir " + dir);
        }
    }
}
}
