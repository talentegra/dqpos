package com.BRMicro;

public interface IUsbConnState {
    void onDeviceNotFound();

    void onUsbConnected();

    void onUsbPermissionDenied();
}
