package com.dreamtech.toolshopapi.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;


public class TestStatusLogger implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("--------------------------------------------------");
        System.out.println("✅ TEST PASSED: " + context.getDisplayName());
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("--------------------------------------------------");
        System.out.println("❌ TEST FAILED: " + context.getDisplayName());
        System.out.println("Reason: " + cause.getMessage());
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        System.out.println("⚠️ TEST ABORTED: " + context.getDisplayName());
    }
}