package diasil.color;

public class ToneMapMaximumToWhite extends ToneMapper
{
    public ToneMapMaximumToWhite(float display_luminance)
	{
		super(display_luminance);
	}
    public float[][] toneMap(XYZImage img)
    {
        float[][] r = new float[img.width()][img.height()];
        float max = Float.NEGATIVE_INFINITY;
        for (int i=0; i<img.width(); ++i)
        {
            for (int j=0; j<img.height(); ++j)
            {
				float y = img.X[i][j].Y;
				if (!Float.isNaN(y) && !Float.isInfinite(y))
				{
					max = Math.max(max, y);
				}
            }
        }
        float scale = display_luminance/max;
        for (int i=0; i<img.width(); ++i)
        {
            for (int j=0; j<img.height(); ++j)
            {
                r[i][j] = scale;
            }
        }
        return r;
    }

}
