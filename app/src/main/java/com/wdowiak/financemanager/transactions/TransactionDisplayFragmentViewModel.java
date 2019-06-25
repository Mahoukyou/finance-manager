package com.wdowiak.financemanager.transactions;

import com.wdowiak.financemanager.commons.DisplayFragmentViewModel;
import com.wdowiak.financemanager.data.Transaction;

// since we cannot get .class of templated type CommonViewModel<TYPE, AdAPTER>
// we have to make empty class derived from the templated one
public class TransactionDisplayFragmentViewModel
        extends DisplayFragmentViewModel<Transaction, TransactionsAdapter>
{

}