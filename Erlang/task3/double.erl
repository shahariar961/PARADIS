-module(double).
-export([start_link/0,double/1]).

start_link() ->
    Pid= spawn(fun() -> double_return() end),
    register(double,Pid).

double_return() ->
    receive
	    {Pid,Ref,N} when is_reference(Ref), is_integer(N) ->
            Pid ! {Ref,N*2};
	    {Pid,Ref,N} ->
            error(badarith)
    end, 
    double_return().
double(N) ->
    Pid=spawn(fun() -> doubler() end),
    Pid! {N}.
    
doubler() ->
    
    receive
        {N}->
            Ref=make_ref(),
            double ! {self(),Ref,N};
        
        {_,N} ->
            N
        end.