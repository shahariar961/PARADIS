-module(monitor).

-export([start_link/0,init/1]).
-behaviour(supervisor).

start_link() ->
    % supervisor:start_link({local, monitor}, ?MODULE, []).
    supervisor:start_link(?MODULE,[]).
% init(_Args) ->
%     double_procs = [        {double, {double, start, []}, permanent, 5000, worker, [double]}
%     ],
%     {ok, {{one_for_one, 5, 10}, double_procs}}.

init(_Args)->
    Flags=#{strategy => one_for_one, intensity => 1 ,period=> 5},
    Spec= [#{id => double_id,start=> {double,start_link,[]}}],
    {ok,{Flags,Spec}}.
