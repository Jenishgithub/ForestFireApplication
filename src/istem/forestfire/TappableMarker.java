package istem.forestfire;

import android.content.Context;
import android.graphics.Bitmap;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Marker;

/**
 * Created by Hp on 6/16/2015.
 */
public class TappableMarker extends Marker
{
    public TappableMarker(Context context ,int user, LatLong localLatLong)
    {
        super(localLatLong,
                AndroidGraphicFactory.convertToBitmap(context.getResources().getDrawable(user)),0,0);
    }

    public TappableMarker(Context context ,int user, LatLong localLatLong,String markertype)
    {

        super(localLatLong,
                AndroidGraphicFactory.convertToBitmap(context.getResources().getDrawable(user)),0,-1*AndroidGraphicFactory.convertToBitmap(context.getResources().getDrawable(user)).getHeight()/2);
    }

    public boolean onTap(LatLong tapLatLong,
                         org.mapsforge.core.model.Point layerXY,
                         org.mapsforge.core.model.Point tapXY)
    {
        return super.onTap(tapLatLong, layerXY, tapXY);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
