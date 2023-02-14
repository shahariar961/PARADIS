-module(monitor).
-export([start/0]).

start()->
    spawn(fun() -> counter() end).
counter() ->
    spawn(fun() -> double:start() end),
    spawn(fun() -> monitor_s() end).
monitor_s() ->
    erlang:monitor(process, double),
    receive 
         {'DOWN',_,process,_,_} ->
             counter();
             _ ->
            monitor_s()
         end.