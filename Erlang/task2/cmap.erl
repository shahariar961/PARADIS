-module(cmap).
-behaviour(gen_worker).
-export([handle_work/1, ordered/2]).
%% Simple handle_work: apply the function to the value
handle_work({Fun, V}) ->
{result, Fun(V)}.
ordered(Fun, L) ->
%% Start a work-pool with 2 workers
WorkPool = gen_worker:start(?MODULE, 2),
%% Schedule the work asynchronously
Refs = [gen_worker:async(WorkPool, {Fun, V}) || V <- L],
%% Await the result
Result = gen_worker:await_all(Refs),
%% Stop our work pool
gen_worker:stop(S),
%% Return the result
Result.
