# WORK IN PROGRESS

# jasperreportchecker
A tool which checks jasperreports JRXML files for unused fields, parameters and variables.


# How it Should Work?

1. Read the JRXML in a structure which contains lists for fields, parameters and variables.
1. Read the JRXML as text.
1. Check for each field, parameter and variable, if there is a call in the JRXML via checking a contain of e. g. "$F{fieldname}".
1. List all fields, parameters and variables which are not used in the JRXML on console.

Extension: Allow to create reports for all JRXML in a directory and its sub directories.
