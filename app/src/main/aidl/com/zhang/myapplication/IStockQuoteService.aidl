// IStockQuoteService.aidl
package com.zhang.myapplication;

// Declare any non-default types here with import statements

interface IStockQuoteService {
    double getQuote(String ticker);
}
