rurl="git@github.com:oskarwh/NeoCodec.git" localdir="NeoCodec" subfolder="cluster" && shift 2

mkdir -p "$localdir"
cd "$localdir"

git init
git remote add -f origin "$rurl"

git config core.sparseCheckout true

# Only fetch cluster folder
echo "$subfolder" >> .git/info/sparse-checkout


git pull origin main
