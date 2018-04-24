# Automated releases
TBD

# Manual releases
1. Check out the right branch and the right commit. This is necessary
when not releasing from the HEAD of master.

2. Tag the right commit and push it to GitHub. This is mandatory if the
release isn't made from the HEAD of master.
	```
	git tag 1.0.0 3aba546aea1235
	git push origin 1.0.0
	```
