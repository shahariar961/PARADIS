-module(monitor).

-export([start_link/0,init/1]).
-behaviour(supervisor).

start_link() ->
    
    supervisor:start_link(?MODULE,[]).


init(_Args)->
    Flags=#{strategy => one_for_one, intensity => 1 ,period=> 5},
    Spec= [#{id => double_id,start=> {double,start_link,[]}}],
    {ok,{Flags,Spec}}.
