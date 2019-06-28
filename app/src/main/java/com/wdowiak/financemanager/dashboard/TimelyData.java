package com.wdowiak.financemanager.dashboard;

import java.util.ArrayList;
import java.util.HashMap;

public class TimelyData
{
    HashMap<CustomDate, SingleTimelyData> timelyDataHashMap;
    ArrayList<SingleTimelyData> sortedTimelyData;
    ArrayList<SingleTimelyData> sortedWithEmptyTimelyData;
    long totalTransactions = 0;
    double totalAmount = 0;
}
