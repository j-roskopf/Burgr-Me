package io.burgrme.Dagger.Components;

import dagger.Component;
import io.burgrme.Activities.MainActivity;
import io.burgrme.Dagger.Modules.AppModule;

/**
 * Created by Joe on 12/21/2016.
 */

@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
}
