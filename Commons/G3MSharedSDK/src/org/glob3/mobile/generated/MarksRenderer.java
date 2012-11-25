package org.glob3.mobile.generated; 
public class MarksRenderer extends LeafRenderer
{
  private final boolean _readyWhenMarksReady;
  private java.util.ArrayList<Mark> _marks = new java.util.ArrayList<Mark>();
  private GLState _glState;

  private G3MContext _context;
  private Camera     _lastCamera;

  private MarkTouchListener _markTouchListener;
  private boolean _autoDeleteMarkTouchListener;


  public MarksRenderer(boolean readyWhenMarksReady)
  {
	  _readyWhenMarksReady = readyWhenMarksReady;
	  _context = null;
	  _lastCamera = null;
	  _markTouchListener = null;
	  _autoDeleteMarkTouchListener = false;
	_glState = new GLState();
	_glState.disableDepthTest();
	_glState.enableBlend();
	_glState.enableTextures();
	_glState.enableTexture2D();
	_glState.enableVerticesPosition();
  }

  public final void setMarkTouchListener(MarkTouchListener markTouchListener, boolean autoDelete)
  {
	if (_autoDeleteMarkTouchListener)
	{
	  if (_markTouchListener != null)
		  _markTouchListener.dispose();
	}

	_markTouchListener = markTouchListener;
	_autoDeleteMarkTouchListener = autoDelete;
  }

  public void dispose()
  {
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  if (_marks.get(i) != null)
		  _marks.get(i).dispose();
	}
  
	if (_autoDeleteMarkTouchListener)
	{
	  if (_markTouchListener != null)
		  _markTouchListener.dispose();
	}
	_markTouchListener = null;
	if (_glState != null)
		_glState.dispose();
  }

  public void initialize(G3MContext context)
  {
	_context = context;
  
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  Mark mark = _marks.get(i);
	  mark.initialize(context);
	}
  }

  public void render(G3MRenderContext rc)
  {
	//  rc.getLogger()->logInfo("MarksRenderer::render()");
  
	// Saving camera for use in onTouchEvent
	_lastCamera = rc.getCurrentCamera();
  
  
	GL gl = rc.getGL();
  
	//gl->enableVerticesPosition();
	//gl->enableTextures();
	//gl->enableBlend();
  
	//gl->disableDepthTest();
	gl.setState(_glState);
  
	final Vector3D radius = rc.getPlanet().getRadii();
	final double minDistanceToCamera = (radius._x + radius._y + radius._z) / 3 * 0.75;
  
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  Mark mark = _marks.get(i);
	  //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());
  
	  if (mark.isReady())
	  {
		mark.render(rc, minDistanceToCamera);
	  }
	}
  
	//gl->enableDepthTest();
	//gl->disableBlend();
  
	//gl->disableTextures();
	//gl->disableVerticesPosition();
	//gl->disableTexture2D();
  }

  public final void addMark(Mark mark)
  {
	_marks.add(mark);
	if (_context != null)
	{
	  mark.initialize(_context);
	}
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
	if (_markTouchListener == null)
	{
	  return false;
	}
  
	boolean handled = false;
  
	if ((touchEvent.getType() == TouchEventType.Down) && (touchEvent.getTouchCount() == 1))
	{
  
	  if (_lastCamera != null)
	  {
		final Vector2I touchedPixel = touchEvent.getTouch(0).getPos();
		final Planet planet = ec.getPlanet();
  
		double minSqDistance = IMathUtils.instance().maxDouble();
		Mark nearestMark = null;
  
		int marksSize = _marks.size();
		for (int i = 0; i < marksSize; i++)
		{
		  Mark mark = _marks.get(i);
  
		  if (!mark.isReady())
		  {
			continue;
		  }
		  if (!mark.isRendered())
		  {
			continue;
		  }
  
		  final int textureWidth = mark.getTextureWidth();
		  if (textureWidth <= 0)
		  {
			continue;
		  }
  
		  final int textureHeight = mark.getTextureHeight();
		  if (textureHeight <= 0)
		  {
			continue;
		  }
  
		  final Vector3D cartesianMarkPosition = planet.toCartesian(mark.getPosition());
		  final Vector2I markPixel = _lastCamera.point2Pixel(cartesianMarkPosition);
  
		  final RectangleI markPixelBounds = new RectangleI(markPixel._x - (textureWidth / 2), markPixel._y - (textureHeight / 2), textureWidth, textureHeight);
  
		  if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y))
		  {
			final double distance = markPixel.sub(touchedPixel).squaredLength();
			if (distance < minSqDistance)
			{
			  nearestMark = mark;
			  minSqDistance = distance;
			}
		  }
		}
  
		if (nearestMark != null)
		{
		  handled = _markTouchListener.touchedMark(nearestMark);
		}
  
	  }
  
	}
  
	return handled;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
	if (_readyWhenMarksReady)
	{
	  int marksSize = _marks.size();
	  for (int i = 0; i < marksSize; i++)
	  {
		if (!_marks.get(i).isReady())
		{
		  return false;
		}
	  }
	}
  
	return true;
  }

  public final void start()
  {

  }

  public final void stop()
  {

  }

  public final void onResume(G3MContext context)
  {
	_context = context;
  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

}