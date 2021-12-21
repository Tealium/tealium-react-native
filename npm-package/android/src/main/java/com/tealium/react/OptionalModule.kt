package com.tealium.react

import com.tealium.core.TealiumConfig

interface OptionalModule {

    /**
     * Delegates configuration to the sub-module. OptionalModules are passed the config
     * instance prior to the Tealium instance creation
     */
    fun configure(config: TealiumConfig)
}