package com.wine.to.up.user.service.configuration.firebase;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public interface FirebaseConfigReadStrategy {
    boolean validate();

    InputStream read() throws IOException, GeneralSecurityException;
}
