package org.glob3.mobile.generated; 
public class RenderContext extends Context
{
  private IGL _gl;
  private Camera _camera;

  public RenderContext(IFactory factory, ILogger logger, Planet planet, IGL gl, Camera camera)
  {
	  super(factory, logger, planet);
	  _gl = gl;
	  _camera = camera;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IGL* getGL() const
  public final IGL getGL()
  {
	return _gl;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Camera* getCamera() const
  public final Camera getCamera()
  {
	return _camera;
  }
}