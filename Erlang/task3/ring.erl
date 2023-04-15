-module(ring).
-export([start/2]).

start(N, M) ->
    Pids = spawn_processes(N),
    send_messages(Pids, M),
    wait_for_termination(Pids),
    N * M.

spawn_processes(N) ->
    build_pid_list(N, [self()]).

build_pid_list(1, Acc) ->
    lists:reverse(Acc);
build_pid_list(N, Acc) ->
    NextPid = spawn(fun() -> loop(lists:last(Acc)) end),
    build_pid_list(N - 1, [NextPid | Acc]).

loop(NextPid) ->
    receive
        {From, Message, M} ->
            NextMessage = Message + 1,
            NextM = M - 1,
            NextFrom = NextPid,
            NextPid ! {From, NextMessage, NextM},
            loop(NextPid);
        {From, _, 0} ->
            From ! done
    end.

send_messages([H|T], M) ->
    FirstPid = H,
    FirstPid ! {self(), 0, M},
    wait_for_termination(T);
send_messages(Pid, M) ->
    FirstPid = Pid,
    FirstPid ! {self(), 0, M}.



wait_for_termination(Pids) ->
    receive
        done ->
            case Pids of
                [] -> ok;
                [H|T] -> H ! {self(), 0, 0}, wait_for_termination(T)
            end
    end.
