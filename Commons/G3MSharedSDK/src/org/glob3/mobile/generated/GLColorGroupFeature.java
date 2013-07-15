package org.glob3.mobile.generated; 
///////////////////////////////////////////////////////////////////////////////////////////
public abstract class GLColorGroupFeature extends GLFeature
{
  private final int _priority;
  private final boolean _blend;
  private final int _sFactor;
  private final int _dFactor;

  public GLColorGroupFeature(int p, boolean blend, int sFactor, int dFactor)
  {
     super(GLFeatureGroupName.COLOR_GROUP);
     _priority = p;
     _blend = blend;
     _sFactor = sFactor;
     _dFactor = dFactor;
//    _globalState = new GLGlobalState();
//
//    if (blend){
//      _globalState->enableBlend();
//      _globalState->setBlendFactors(sFactor, dFactor);
//    } else{
//      _globalState->disableBlend();
//    }

  }
  public final int getPriority()
  {
     return _priority;
  }

  public final void blendingOnGlobalGLState(GLGlobalState state)
  {
    if (_blend)
    {
      state.enableBlend();
      state.setBlendFactors(_sFactor, _dFactor);
    }
    else
    {
      state.disableBlend();
    }
  }
}