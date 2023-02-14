-module(bank).
-export([start/0,balance/2,deposit/3,lend/4]).

start() ->
    spawn(fun() -> loop(#{}) end).
    %spawn(fun()-> monitor() end).

balance(Pid, Who) ->
    try 
        Ref = make_ref(),
        Pid ! {balance, self(), Ref, Who},
        receive
            {ok, Ref, Balance} -> {ok, Balance};
            {no_account, Ref, Who} -> {no_account, Who}
        end
    catch
        error:badarg ->
            no_bank
    end.
deposit(Pid,Who, X) ->
    try
    Ref=make_ref(),
    Pid ! {deposit, self(),Ref, Who, X},
    receive        
    {Ref,ok, NewBalance} -> {ok, NewBalance};
{Ref,no_account} -> {no_account, Who}
    end
    catch
        error:badarg ->
            no_bank
end.


withdraw(Pid, Who, X) ->
    try
Ref=make_ref(),
Pid ! {withdraw, self(),Ref, Who, X},
receive
{Ref,ok, AmountLeft} -> {ok, AmountLeft};
{Ref,no_account,Who} -> {no_account, Who};
{Ref,insufficient_funds} -> insufficient_funds
    end
    catch
        error:badarg ->
            no_bank
end.

lend(Pid,From, To, X) ->
    try
Ref=make_ref(),
Pid ! {lend, self(), From,Ref, To, X},
receive
{Ref,ok} -> ok;
{Ref,no_account, Who} -> {no_account, Who};
{Ref,insufficient_funds} -> insufficient_funds
    end
    catch
        error:badarg ->
            no_bank
end.

monitor() ->
    receive 
         {'DOWN',_,process,_,_} ->
            nobank,
             start();
             _ ->
            monitor()
         end.
loop(Accounts) ->
    erlang:monitor(process,self()),
    
    receive
        {balance, Pid,Ref,Who} ->
            case is_map_key(Who, Accounts) of
                false ->
                    Pid ! {no_account, Ref,Who},
                    loop(Accounts);
                true ->
                    Balance=map_get(Who, Accounts),
                    Pid ! {ok,Ref,Balance},
                    loop(Accounts)
                end;
        {deposit, Pid, Ref,Who, X} ->
            case is_map_key(Who, Accounts) of
                false ->
                    NewAccounts = maps:put(Who, X, Accounts),
                    Pid ! {Ref,ok,X},
                    loop(NewAccounts);
                true ->
                    NewBalance = maps:get(Who, Accounts) + X,
                    NewAccounts = maps:put(Who, NewBalance, Accounts),
                    Pid ! {Ref,ok, NewBalance},
                    loop(NewAccounts)
                end;
        {withdraw, Pid,Ref, Who, X} ->
            case is_map_key(Who, Accounts) of
                false ->
                    Pid ! {Ref,no_account, Who},
                    loop(Accounts);
                true ->
                    Balance=maps:get(Who, Accounts),
                    if Balance>= X ->
                        NewBalance = Balance - X,
                        NewAccounts = maps:put(Who,NewBalance, Accounts),
                        Pid ! {Ref,ok,NewBalance},
                        loop(NewAccounts);
                    true ->
                            Pid ! {Ref,insufficient_funds},
                            loop(Accounts)
                    end
                end;
        {lend, Pid, From,Ref, To, X}  ->
            case {is_map_key(From,Accounts), is_map_key(To,Accounts)} of
                {false, false} ->
                    Pid ! {Ref,no_account, both},
                    loop(Accounts);
                {false, _} ->
                    Pid ! {Ref,no_account, From},
                    loop(Accounts);
                {_, false} ->
                    Pid ! {Ref,no_account, To},
                    loop(Accounts);
                {_, _} ->
                    FromBalance = maps:get(From, Accounts),
                    ToBalance = maps:get(To, Accounts),
                    if FromBalance >= X ->
                        NewFromBalance = FromBalance - X,
                        NewToBalance = ToBalance + X,
                        NewAccounts = maps:put(From, NewFromBalance, Accounts),
                        NewAccounts1 = maps:put(To, NewToBalance, NewAccounts),
                        Pid ! {Ref,ok},
                        loop(NewAccounts1);
                    true ->
                        Pid ! {Ref,insufficient_funds},
                        loop(Accounts)
                end         
           
        end;
        _->
            monitor()

    end.









