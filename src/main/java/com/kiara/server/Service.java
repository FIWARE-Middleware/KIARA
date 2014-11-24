package com.kiara.server;

import com.kiara.server.Servant;
import java.util.List;

public interface Service {

    public void register(Object serviceImpl);

    public List<Servant> getGeneratedServants();
}
