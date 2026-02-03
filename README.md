# ML-QuadratPP (ML2++)

**ML2++** is a model-driven framework and domain-specific modeling language (DSL) for building **IoT + Machine Learning** applications with a strong focus on **time-series forecasting** and automated ML workflows.

With ML2++, users define:
- **IoT system structure** (things/components, messages, ports, deployment aspects)
- **Data analytics configuration** (input/output features, preprocessing steps, ML/forecasting model settings)
- **Training and evaluation settings** (splits, metrics, plots, and reporting)

ML2++ aims to reduce manual coding by generating the key artifacts needed to run an ML-enabled IoT pipeline, including:
- preprocessing configuration and scripts
- model training and prediction workflow (e.g., forecasting for the next *k* steps)
- evaluation outputs (metrics and visualizations)
- example datasets and use-case models

## Main components

- **Textual Editor (Xtext / Eclipse / Maven):** write and validate ML2++ models as text.
- **Graphical Editor (Sirius Web + Docker):** create ML2++ models visually in a web environment.
- **Examples:** sample models and small datasets for quick experimentation.
- **Documentation:** tutorials and paper PDFs describing the language and tool.

> If you use this repository in academic work, please cite the corresponding papers in `docs/papers/`.
## Links

### Papers
- [FSE Tool Paper (PDF)](docs/papers/FSE_Tool_Paper.pdf)
- [MODELSWARD 2025 Paper (PDF)](docs/papers/Mardani_2025_MODELSWARD.pdf)
- [MoDIoT Paper (PDF)](docs/papers/MoDIoT_2025.pdf)

### Tutorials
- [Graphical editor tutorial (PDF)](docs/tutorials/graphical_tutorial.pdf)
- [Textual editor tutorial (PDF)](docs/tutorials/textual_tutorial.pdf)
- [Use cases (PDF)](docs/tutorials/use_cases.pdf)

### Editors
- **Textual editor (ML2++)**: [editors/textual/ml2pp-textual](editors/textual/ml2pp-textual)
- **Textual editor (ML2 baseline)**: [editors/textual/ml2-textual](editors/textual/ml2-textual)
- **Textual editor (web version)**: [editors/textual/web](editors/textual/web)
- **Graphical editor (Docker/Sirius Web)**: [editors/graphical/ml2pp-graphical](editors/graphical/ml2pp-graphical)

### Examples
- [Examples overview](examples/README.md)
- [Datasets (CSV)](examples/datasets)
- [Graphical examples](examples/graphical)
- [Textual examples](examples/textual)
