package ar.edu.itba.pam.nearchatter.di

import android.content.Context

class ProductionNearchatterContainer(context: Context) : NearchatterContainer {
    private val context = context.applicationContext;

    fun getApplicationContext(): Context {
        return context;
    }
}