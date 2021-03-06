package org.glob3.mobile.generated; 
public class FrameTasksExecutor
{
  private final int _minimumExecutionsPerFrame;
  private final int _maximumExecutionsPerFrame;
  private final int _maximumQueuedTasks;
  private final long _maxTimePerFrameMS;
  private final long _maxTimePerFrameStressedMS;

  private java.util.LinkedList<FrameTask> _preRenderTasks = new java.util.LinkedList<FrameTask>();

  private boolean canExecutePreRenderStep(G3MRenderContext rc, int executedCounter)
  {
    final int tasksCount = _preRenderTasks.size();
    if (tasksCount <= _minimumExecutionsPerFrame)
    {
      //if (_stressed) {
      //  rc->getLogger()->logWarning("Abandon STRESSED mode");
      //}
      _stressed = false;
    }
  
    if (tasksCount == 0)
    {
      return false;
    }
  
    if (executedCounter < _minimumExecutionsPerFrame)
    {
      return true;
    }
  
    if (tasksCount > _maximumQueuedTasks)
    {
      //if (!_stressed) {
      //  rc->getLogger()->logWarning("Too many queued tasks (%d). Goes to STRESSED mode",
      //                              _preRenderTasks.size());
      //}
      _stressed = true;
    }
  
    if (_stressed)
    {
      return rc.getFrameStartTimer().elapsedTimeInMilliseconds() < _maxTimePerFrameStressedMS;
    }
  
    if (executedCounter >= _maximumExecutionsPerFrame)
    {
      return false;
    }
    return rc.getFrameStartTimer().elapsedTimeInMilliseconds() < _maxTimePerFrameMS;
  }

  private boolean _stressed;

  public FrameTasksExecutor()
  {
     _minimumExecutionsPerFrame = 1;
     _maximumExecutionsPerFrame = 8;
     _maximumQueuedTasks = 64;
     _maxTimePerFrameMS = 5;
     _maxTimePerFrameStressedMS = 25;
     _stressed = false;

  }

  public final void addPreRenderTask(FrameTask preRenderTask)
  {
    _preRenderTasks.addLast(preRenderTask);
  }

  public final void doPreRenderCycle(G3MRenderContext rc)
  {
  
    //  int canceledCounter = 0;
    java.util.Iterator<FrameTask> i = _preRenderTasks.iterator();
    while (i.hasNext())
    {
      FrameTask task = i.next();
  
      final boolean isCanceled = task.isCanceled(rc);
      if (isCanceled)
      {
        if (task != null)
           task.dispose();
        i.remove();
        //      canceledCounter++;
      }
    }
  
    //  if (canceledCounter > 0) {
    //    rc->getLogger()->logInfo("Removed %d tasks, actived %d tasks.",
    //                             canceledCounter,
    //                             _preRenderTasks.size());
    //  }
  
    int executedCounter = 0;
    while (canExecutePreRenderStep(rc, executedCounter))
    {
      FrameTask task = _preRenderTasks.getFirst();
      _preRenderTasks.removeFirst();
  
      task.execute(rc);
  
      if (task != null)
         task.dispose();
  
      executedCounter++;
    }
  
    //  if (false) {
    //    //    if ( rc->getFrameStartTimer()->elapsedTime().milliseconds() > _maxTimePerFrame.milliseconds()*3 ) {
    //    //      rc->getLogger()->logWarning("doPreRenderCycle() took too much time, Tasks: canceled=%d, executed=%d in %ld ms, queued %d. STRESSED=%d",
    //    //                                  canceledCounter,
    //    //                                  executedCounter,
    //    //                                  rc->getFrameStartTimer()->elapsedTime().milliseconds(),
    //    //                                  _preRenderTasks.size(),
    //    //                                  _stressed);
    //    //
    //    //    }
    //    //    else {
    //    if ((executedCounter > 0) ||
    //        (canceledCounter > 0) ||
    //        (_preRenderTasks.size() > 0)) {
    //      rc->getLogger()->logInfo("Tasks: canceled=%d, executed=%d in %ld ms, queued %d. STRESSED=%d",
    //                               canceledCounter,
    //                               executedCounter,
    //                               rc->getFrameStartTimer()->elapsedTime().milliseconds(),
    //                               _preRenderTasks.size(),
    //                               _stressed);
    //    }
    //    //    }
    //  }
  
  }

  public void dispose()
  {

  }

}