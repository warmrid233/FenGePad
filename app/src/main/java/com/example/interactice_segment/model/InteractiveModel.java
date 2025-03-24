package com.example.interactice_segment.model;

import android.graphics.Bitmap;

import com.example.interactice_segment.model.tool.FinishTask;
import com.example.interactice_segment.model.tool.GetImageCallback;
import com.example.interactice_segment.model.tool.GetImageTask;
import com.example.interactice_segment.model.tool.UndoTask;
import com.example.interactice_segment.model.tool.UploadBitmapTask;
import com.example.interactice_segment.model.tool.UploadClickTask;

public class InteractiveModel implements IInteractiveModel
{
    private String ip_port = null;

    public InteractiveModel(String ip_port)
    {
        this.ip_port = ip_port;
    }

    @Override
    public void uploadClick(float originalX, float originalY, int isPositive)
    {
        UploadClickTask uct = new UploadClickTask();
        uct.setIp_port(ip_port);
        uct.execute(originalX, originalY, (float) isPositive);
    }

    @Override
    public void getImage(GetImageCallback callback)
    {
        GetImageTask git = new GetImageTask(callback);
        git.setIp_port(ip_port);
        git.execute();
    }

    @Override
    public void finish()
    {
        FinishTask ft = new FinishTask();
        ft.setIp_port(ip_port);
        ft.execute();
    }

    @Override
    public void undo()
    {
        UndoTask ut = new UndoTask();
        ut.setIp_port(ip_port);
        ut.execute();
    }

}
