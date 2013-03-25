package org.glob3.mobile.generated; 
public class TextureUploader extends IImageListener
{
  private TileTextureBuilder _builder;

  private final java.util.ArrayList<RectangleI> _rectangles;

  private final String _textureId;

  public TextureUploader(TileTextureBuilder builder, java.util.ArrayList<RectangleI> rectangles, String textureId)
  {
     _builder = builder;
     _rectangles = rectangles;
     _textureId = textureId;

  }

  public final void imageCreated(IImage image)
  {
    _builder.imageCreated(image, _rectangles, _textureId);
  }
}