package com.example.aplikasibukudigitalkewirausahaanbagipemula.utils

class KataAcak {
    fun getHurufSaja(): String{
        var str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var hurufAcak = "1"
        for(i in 1..12){
            hurufAcak+=str.random()
        }
        return hurufAcak
    }
    fun getHurufDanAngka(): String{
        var str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        var hurufAcak = "1"
        for(i in 1..12){
            hurufAcak+=str.random()
        }
        return hurufAcak
    }
}