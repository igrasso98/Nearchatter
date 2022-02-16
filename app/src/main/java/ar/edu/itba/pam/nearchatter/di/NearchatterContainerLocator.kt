package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import androidx.annotation.VisibleForTesting

class NearchatterContainerLocator {
    companion object {
        var container: NearchatterContainer? = null
        private set
    }

    fun locateComponent(context: Context): NearchatterContainer {
        if (container == null) {
            setComponent(ProductionNearchatterContainer(context));
        }
        return container!!
    }

    @VisibleForTesting
    fun setComponent(nearchatterContainer: NearchatterContainer) {
        container = nearchatterContainer
    }
}