package diasil.math.geometry2;

import diasil.math.Matrix3x3;

public final class MTransform2 implements Transform2
{

    public Matrix3x3 M, I;

    public MTransform2()
    {
        M = new Matrix3x3();
        I = new Matrix3x3();
    }

    public MTransform2(Matrix3x3 m, Matrix3x3 i)
    {
        M = new Matrix3x3(m);
        I = new Matrix3x3(i);
    }

    public MTransform2 multiply(MTransform2 t)
    {
        return new MTransform2(M.multiply(t.M), t.I.multiply(I));
    }

    public Point2 toScreenSpace(Point2 p)
    {
        float tx = M.X00 * p.X + M.X01 * p.Y + M.X02;
        float ty = M.X10 * p.X + M.X11 * p.Y + M.X12;
        float w  = M.X20 * p.X + M.X21 * p.Y + M.X22;
        if (w != 1.0f)
        {
            float inv_w = 1.0f / w;
            return new Point2(tx * inv_w, ty * inv_w);
        }
        return new Point2(tx, ty);
    }

    public Point2 toRasterSpace(Point2 p)
    {
        float tx = I.X00 * p.X + I.X01 * p.Y + I.X02;
        float ty = I.X10 * p.X + I.X11 * p.Y + I.X12;
        float w  = I.X20 * p.X + I.X21 * p.Y + I.X22;
        if (w != 1.0f)
        {
            float inv_w = 1.0f / w;
            return new Point2(tx * inv_w, ty * inv_w);
        }
        return new Point2(tx, ty);
    }

    public Vector2 toScreenSpace(Vector2 v)
    {
        return new Vector2(M.X00 * v.X + M.X01 * v.Y,
                           M.X10 * v.X + M.X11 * v.Y);
    }

    public Vector2 toRasterSpace(Vector2 v)
    {
        return new Vector2(I.X00 * v.X + I.X01 * v.Y,
                           I.X10 * v.X + I.X11 * v.Y);
    }


    public void setAsIdentity()
    {
        M.setAsIdentity();
        I.setAsIdentity();
    }
    
    public static MTransform2 createIdentity()
    {
        MTransform2 r = new MTransform2();
        r.setAsIdentity();
        return r;
    }

    public void translate(float x, float y)
    {
        M.X02 += x;
        M.X12 += y;

        I.X02 -= x;
        I.X12 -= y;
    }

    public void scale(float x, float y)
    {
        M.X00 *= x;
        M.X11 *= y;

        I.X00 /= x;
        I.X11 /= y;
    }

    public void setAsRotator(float t)
    {
        setAsIdentity();

        float cost = (float)Math.cos(t);
        float sint = (float)Math.sin(t);

        M.X00 = cost;
        M.X01 = -sint;
        M.X10 = sint;
        M.X11 = cost;

        I.X00 = cost;
        I.X01 = sint;
        I.X10 = -sint;
        I.X11 = cost;
    }
	
	public void fitImage(int w, int h)
	{
		fitImage(w, h, Math.max(w, h));
	}
	public void fitImage(int w, int h, float s)
	{
		// flip y, move to center of pixel
		scale(1.0f, -1.0f);
		translate(0.5f, h - 1 + 0.5f);
		
		// change [0,w]x[0,h] to [-1,1]x[-1,1]
		scale(2.0f/s, 2.0f/s);
		translate(-1, -1);
	}
}
