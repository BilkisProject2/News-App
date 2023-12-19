package com.example.newsapp.RoomDatabase

import androidx.room.TypeConverter
import androidx.room.TypeConverters


class Converter {

    @TypeConverter
    fun fromSource(source: com.example.newsapp.Model.Source):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String):com.example.newsapp.Model.Source{
        return  com.example.newsapp.Model.Source(name,name)
    }

}