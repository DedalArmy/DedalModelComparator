#!/bin/bash

sshpass -p $1 scp -r qperez@159.31.103.18:$2 $3 || exit;
wait $!