package org.glob3.mobile.generated; 
public class SimpleTextureMapping extends TextureMapping
{
  private TextureIDReference _glTextureId;

  private IFloatBuffer _texCoords;
  private final boolean _ownedTexCoords;

  private MutableVector2D _translation = new MutableVector2D();
  private MutableVector2D _scale = new MutableVector2D();

  private final boolean _transparent;

  private void releaseGLTextureId()
  {
  
    if (_glTextureId != null)
    {
      _glTextureId.dispose();
      _glTextureId = null;
    }
    else
    {
      ILogger.instance().logError("Releasing invalid simple texture mapping");
    }
  }


  public SimpleTextureMapping(TextureIDReference glTextureId, IFloatBuffer texCoords, boolean ownedTexCoords, boolean transparent)
  {
     _glTextureId = glTextureId;
     _texCoords = texCoords;
     _translation = new MutableVector2D(0, 0);
     _scale = new MutableVector2D(1, 1);
     _ownedTexCoords = ownedTexCoords;
     _transparent = transparent;

  }

  public final void setTranslationAndScale(Vector2D translation, Vector2D scale)
  {
    _translation = translation.asMutableVector2D();
    _scale = scale.asMutableVector2D();
  }

  public void dispose()
  {
    if (_ownedTexCoords)
    {
      if (_texCoords != null)
         _texCoords.dispose();
    }
  
    releaseGLTextureId();
  
    super.dispose();
  }

  public final IGLTextureId getGLTextureId()
  {
    return _glTextureId.getID();
  }

  public final IFloatBuffer getTexCoords()
  {
    return _texCoords;
  }

  public final void modifyGLState(GLState state)
  {
    if (_texCoords == null)
    {
      ILogger.instance().logError("SimpleTextureMapping::bind() with _texCoords == NULL");
    }
    else
    {
      state.clearGLFeatureGroup(GLFeatureGroupName.COLOR_GROUP);
  
      if (!_scale.isEquals(1.0, 1.0) || !_translation.isEquals(0.0, 0.0))
      {
        state.addGLFeature(new TextureGLFeature(_glTextureId.getID(), _texCoords, 2, 0, false, 0, _transparent, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), true, _translation.asVector2D(), _scale.asVector2D()), false); //TRANSFORM - BLEND
      }
      else
      {
        state.addGLFeature(new TextureGLFeature(_glTextureId.getID(), _texCoords, 2, 0, false, 0, _transparent, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), false, Vector2D.zero(), Vector2D.zero()), false); //TRANSFORM - BLEND
      }
    }
  }

}